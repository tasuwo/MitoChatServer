package net.tasuwo.mitochat.model

// 空の constructor がないと、Jackson で deserialize するときに例外をはくので、
// デフォルト引数を与える
data class ErrorResponse(val message: String = "")
