package com.wallet.repository;

import com.wallet.model.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IdempotencyRecordRepository
        extends JpaRepository<IdempotencyRecord, Long> {

    @Modifying
    @Query(value = """
            INSERT INTO idempotency_records
                (user_id, idempotency_key, created_at)
            VALUES
                (:userId, :idempotencyKey, CURRENT_TIMESTAMP)
            ON CONFLICT (user_id, idempotency_key)
            DO NOTHING
            """, nativeQuery = true)
    int tryCreate(
            @Param("userId") long userId,
            @Param("idempotencyKey") String idempotencyKey
    );

    Optional<IdempotencyRecord> findByUserIdAndIdempotencyKey(
            long userId,
            String idempotencyKey
    );
}