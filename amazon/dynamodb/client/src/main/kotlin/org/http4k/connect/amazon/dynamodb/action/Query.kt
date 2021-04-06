package org.http4k.connect.amazon.dynamodb.action

import org.http4k.connect.Http4kConnectAction
import org.http4k.connect.Paged
import org.http4k.connect.PagedAction
import org.http4k.connect.amazon.dynamodb.DynamoDbMoshi
import org.http4k.connect.amazon.model.IndexName
import org.http4k.connect.amazon.model.Item
import org.http4k.connect.amazon.model.Key
import org.http4k.connect.amazon.model.TableName
import se.ansman.kotshi.JsonSerializable

@Http4kConnectAction
@JsonSerializable
data class Query(
    val TableName: TableName,
    val KeyConditionExpression: String? = null,
    val FilterExpression: String? = null,
    val ProjectionExpression: String? = null,
    val IndexName: IndexName? = null,
    val ExpressionAttributeNames: TokensToNames? = null,
    val ExpressionAttributeValues: TokensToValues? = null,
    val Select: Select? = null,
    val ConsistentRead: Boolean? = null,
    val ExclusiveStartKey: Key? = null,
    val Limit: Int? = null,
    val ReturnConsumedCapacity: ReturnConsumedCapacity? = null,
    val ScanIndexForward: Boolean? = null,
) : DynamoDbAction<QueryResponse>(QueryResponse::class, DynamoDbMoshi),
    PagedAction<Key, Item, QueryResponse, Query> {
    override fun next(token: Key) = copy(ExclusiveStartKey = token)
}

@JsonSerializable
data class QueryResponse(
    internal val Items: List<ItemResult>? = null,
    val ConsumedCapacity: ConsumedCapacity? = null,
    val Count: Int? = null,
    val LastEvaluatedKey: Key? = null,
    val ScannedCount: Int? = null) : Paged<Key, Item> {
    override val items = Items?.map(ItemResult::toItem) ?: emptyList()
    override fun token() = LastEvaluatedKey
}

enum class Select {
    ALL_ATTRIBUTES, ALL_PROJECTED_ATTRIBUTES, SPECIFIC_ATTRIBUTES, COUNT
}
