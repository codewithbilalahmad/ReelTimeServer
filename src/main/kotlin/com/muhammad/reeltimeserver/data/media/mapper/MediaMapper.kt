package com.muhammad.reeltimeserver.data.media.mapper

import com.muhammad.reeltimeserver.data.media.model.Media
import com.muhammad.reeltimeserver.data.media.model.request.MediaRequest
import com.muhammad.reeltimeserver.data.media.model.response.MediaResponse

fun Media.toMediaResponse() : MediaResponse{
    return MediaResponse(
        mediaId = mediaId,

        isLiked = isLiked,
        isBookmarked = isBookmarked,

        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        popularity = popularity,
        voteCount = voteCount,
        adult = adult,
        mediaType = mediaType,
        originCountry = originCountry,
        originalTitle = originalTitle,
        category = category,
        genreIds = genreIds,

        runTime = runTime,
        tagLine = tagLine,

        videosIds = videosIds,
        similarMediaIds = similarMediaIds
    )
}
fun MediaRequest.toMedia() : Media{
    return Media(
        mediaId = mediaId,

        isLiked = isLiked,
        isBookmarked = isBookmarked,

        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        popularity = popularity,
        voteCount = voteCount,
        adult = adult,
        mediaType = mediaType,
        originCountry = originCountry,
        originalTitle = originalTitle,
        category = category,
        genreIds = genreIds,

        runTime = runTime,
        tagLine = tagLine,

        videosIds = videosIds,
        similarMediaIds = similarMediaIds
    )
}