package org.http4k.connect.kafka.httpproxy.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class GetOffsetsRequest(
    val partitions: List<PartitionOffsetRequest>
)
