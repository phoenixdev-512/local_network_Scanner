package com.example.local_network_scanner.services

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * GitHub API service for fetching repository documentation
 */
interface GitHubApiService {
    /**
     * Fetch the README content from a GitHub repository
     * 
     * @param owner Repository owner (e.g., "phoenixdev-512")
     * @param repo Repository name (e.g., "local_network_Scanner")
     * @param accept Media type for raw content
     * @return Raw README content as string
     */
    @GET("repos/{owner}/{repo}/readme")
    suspend fun getReadme(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Header("Accept") accept: String = "application/vnd.github.v3.raw"
    ): String
    
    /**
     * Fetch file content from a GitHub repository
     * 
     * @param owner Repository owner
     * @param repo Repository name
     * @param path File path in the repository
     * @return File content as string
     */
    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getFileContent(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Header("Accept") accept: String = "application/vnd.github.v3.raw"
    ): String
}
