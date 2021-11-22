package com.example.altbalajiproject.repository

sealed class ResponseData<T>(val data: T?=null,val errorMessage:String?=null)
{
    class Loading<T> :ResponseData<T>()
    class Success<T>(data:T?=null) :ResponseData<T>(data=data)
    class Error<T>(errorMessage: String?):ResponseData<T>(errorMessage = errorMessage)
    
}
