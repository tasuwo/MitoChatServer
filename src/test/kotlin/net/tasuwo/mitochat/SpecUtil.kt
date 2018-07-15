package net.tasuwo.mitochat

import kotlin.reflect.KClass

class SpecUtil<T : Any> {
    companion object {
        fun <T : Any> getFile(targetClass: KClass<T>, fileName: String) : String {
            val packagePath = targetClass.qualifiedName!!
                .replace(".", "/")

            return System.getProperty("user.dir") + "/src/test/resources/" + packagePath + "/" + fileName
        }
    }
}
