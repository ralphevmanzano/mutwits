package com.ralphevmanzano.mutwits.data.models

sealed class Resource<out T> {
	object Loading: Resource<Nothing>()
	class Success<out T>(val data: T): Resource<T>()
	class Error(val message: String): Resource<String>()
}