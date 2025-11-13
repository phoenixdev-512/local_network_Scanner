package com.example.local_network_scanner.data.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val isActive: Boolean = false
)

data class ProfileWithRules(
    @Embedded val profile: Profile,
    @Relation(
        parentColumn = "id",
        entityColumn = "profileId"
    )
    val rules: List<ProfileRule>
)
