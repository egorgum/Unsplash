package com.example.unsplash.ui.auth


import com.example.unsplash.data.modelPublucAccount.PublicInfo
import com.example.unsplash.data.modelsAccount.AccountInfo
import com.example.unsplash.data.modelsCollections.CollectionPhoto
import com.example.unsplash.data.modelsDetail.DetailedPhotoInfo
import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.example.unsplash.data.modelsSearch.SearchResult
import com.example.unsplash.states.AuthConfiguration
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AuthREST {

    @POST("/oauth/token")
    suspend fun getTokenRest(
        @Query("code")code: String,
        @Query("client_id")client_id: String = AuthConfiguration.ACCESS_KEY,
        @Query("client_secret")client_secret: String = AuthConfiguration.SECRET_KEY,
        @Query("redirect_uri")redirect_uri: String = AuthConfiguration.REDIRECT_URI,
        @Query("grant_type")grant_type: String = "authorization_code"
    ): ResponseToken

    @GET("/photos/{id}")
    suspend fun getDetailedPhotoApi(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): DetailedPhotoInfo

    @GET("/photos")
    suspend fun getPhotosList(@Query("page")page: Int, @Header("Authorization")authorization: String):List<PhotosItem>

    @POST("/photos/{id}/like")
    suspend fun like(@Path("id") id:String, @Header("Authorization") authHeader:String)

    @DELETE("/photos/{id}/like")
    suspend fun unlike(@Path("id") id:String, @Header("Authorization") authHeader:String)

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") authHeader:String
    ): SearchResult

    @GET("/collections")
    suspend fun searchCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") authHeader:String
    ):List<CollectionPhoto>

    @GET("/collections/{id}/photos")
    suspend fun searchCollectionsPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") authHeader:String
    ):List<PhotosItem>

    @GET("/me")
    suspend fun searchMe(
        @Header("Authorization") authHeader:String
    ):AccountInfo

    @GET("/users/{username}")
    suspend fun searchPublicMe(
        @Path("username") username: String,
        @Header("Authorization") authHeader:String
    ):PublicInfo

    @GET("/users/{username}/likes")
    suspend fun searchLiked(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") authHeader:String
    ):List<PhotosItem>

}

object RetrofitServiceAuth {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://unsplash.com")
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .build()
    val searchAuth: AuthREST = retrofit.create(AuthREST::class.java)
}

object RetrofitService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.unsplash.com")
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .build()
    val searchApi: AuthREST = retrofit.create(AuthREST::class.java)
}

@JsonClass(generateAdapter = true)
data class ResponseToken(
    @Json(name = "access_token")
    val access_token: String
)