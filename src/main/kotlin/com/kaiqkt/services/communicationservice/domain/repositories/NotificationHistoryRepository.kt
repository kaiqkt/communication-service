package com.kaiqkt.services.communicationservice.domain.repositories

import com.kaiqkt.services.communicationservice.domain.entities.NotificationHistory
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationHistoryRepository : MongoRepository<NotificationHistory, String>, NotificationHistoryRepositoryCustom