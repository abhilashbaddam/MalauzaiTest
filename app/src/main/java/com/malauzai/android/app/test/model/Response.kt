package com.malauzai.android.app.test.model

data class Response(
	val photos: List<PhotosItem>
)

data class Camera(
	val full_name: String? = null,
	val name: String? = null,
	val id: Int,
	val rover_id: Int
)

data class Rover(
	val name: String? = null,
	val id: Int,
	val launch_date: String? = null,
	val landing_date: String? = null,
	val status: String? = null
)

data class PhotosItem(
	val sol: Int? = null,
	val earth_date: String? = null,
	val id: Int,
	val camera: Camera? = null,
	val rover: Rover? = null,
	val img_src: String? = null
)

