package mz.co.zonal.utils

import retrofit2.HttpException
import java.io.IOException

class APIException (message: String): IOException(message)
class NoInternetException (message: String): IOException(message)