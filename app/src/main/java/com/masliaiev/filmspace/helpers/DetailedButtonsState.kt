package com.masliaiev.filmspace.helpers

sealed class DetailedButtonsState

object Progress : DetailedButtonsState()
object Error : DetailedButtonsState()
object ApiError : DetailedButtonsState()
class Result(val inList: Boolean, val description: String = "") : DetailedButtonsState()

