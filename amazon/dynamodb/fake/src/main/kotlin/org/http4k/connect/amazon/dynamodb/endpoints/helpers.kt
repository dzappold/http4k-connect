package org.http4k.connect.amazon.dynamodb.endpoints

import org.http4k.connect.amazon.dynamodb.DynamoDbMoshi
import org.http4k.connect.amazon.dynamodb.model.Item

inline fun <reified OUT : Any> convert(input: Any) = DynamoDbMoshi.asA<OUT>(DynamoDbMoshi.asFormatString(input))

fun Item.asItemResult(): Map<String, Map<String, Any>> =
    mapKeys { it.key.value }.mapValues { convert(it.value) }
