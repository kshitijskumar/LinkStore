package com.example.linkstore.features.savelink.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.linkstore.features.savelink.data.models.entities.LinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(linkEntity: LinkEntity)

    @Query("SELECT * FROM links_table")
    fun getAllLinksAsFlow(): Flow<List<LinkEntity>>

    @Query("SELECT groupName FROM links_table ORDER BY storingTimeStamp DESC LIMIT :count")
    suspend fun getRecentXNumberOfGroupName(count: Int): List<String>

    @Query("SELECT * FROM links_table WHERE groupName = :groupName ORDER BY storingTimeStamp DESC")
    fun getAllLinksForGroupNameAsFlow(groupName: String): Flow<List<LinkEntity>>

    @Query("DELETE FROM links_table WHERE originalLink = :link")
    suspend fun deleteLink(link: String)

}