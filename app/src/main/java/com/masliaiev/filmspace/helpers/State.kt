package com.masliaiev.filmspace.helpers

sealed class State

object Progress : State()
object Error : State()
object ApiError : State()
class Result(val inList: Boolean, val description: String = "") : State()

