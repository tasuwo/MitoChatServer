require 'yaml'

event_infos = {}

swagger_data = open('./build/docs/swagger.yaml', 'r') { |f| YAML.load(f) }
swagger_data["paths"].each{|api_path, api_info|
  api_info.each{|http_method, config|
    # SAM テンプレートへの追記
    # TODO: operationId が同一のものがあると動かない
    event_infos[config["operationId"]] = {
        'Type' => 'Api',
        'Properties' => {
            'Path' => api_path,
            'Method' => http_method,
            'RestApiId' => { 'Ref' => 'MitoChatApi' }
        }
    }

    # Swagger 記述への追記
    # TODO: JAX-RS に全て任せたい
    if http_method != "options"
      swagger_data["paths"][api_path][http_method]['x-amazon-apigateway-integration']['uri'] = {
          'Fn::Sub' => 'arn:aws:apigateway:${AWS::Region}:lambda:path/2015-03-31/functions/${MitoChatFunction.Arn}/invocations'
      }
      swagger_data["paths"][api_path][http_method]['x-amazon-apigateway-integration']['responses'] = {
          'default' => {
              'statusCode' => '200',
              'responseParameters' => {
                  # 'method.response.header.Access-Control-Allow-Headers' => "'Content-Type,X-Amz-Date,Authorization,X-Api-Key'",
                  'method.response.header.Access-Control-Allow-Headers' => "'*'",
                  'method.response.header.Access-Control-Allow-Methods' => "'*'",
                  'method.response.header.Access-Control-Allow-Origin' => "'*'"
              }
          }
      }
    end
  }

  # CORS 対応のために OPTION ヘッダーを付与
  swagger_data["paths"][api_path]['options']['x-amazon-apigateway-integration']['responses'] = {
      'default' => {
          'statusCode' => '200',
          'responseParameters' => {
              'method.response.header.Access-Control-Allow-Headers' => "'*'",
              'method.response.header.Access-Control-Allow-Methods' => "'*'",
              'method.response.header.Access-Control-Allow-Origin' => "'*'"
          },
          'responseTemplates' => {
              'application/json' => "{}"
          }
      }
  }
}

YAML.dump(swagger_data, File.open('./generated/swagger.yaml', 'w'))

sam_data = open('./scripts/template-sam.yaml', 'r') { |f| YAML.load(f) }
sam_data["Resources"]["MitoChatFunction"]["Properties"]["Events"] = event_infos
YAML.dump(sam_data, File.open('./generated/sam.yaml', 'w'))
