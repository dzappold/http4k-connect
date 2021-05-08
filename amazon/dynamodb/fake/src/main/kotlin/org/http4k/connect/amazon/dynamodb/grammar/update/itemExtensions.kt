package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.model.Item
import org.http4k.connect.amazon.dynamodb.model.TokensToNames
import org.http4k.connect.amazon.dynamodb.model.TokensToValues

/**
 * Transform the input item by applying the update to the fields in it.
 */
fun Item.update(
    updateExpression: String?,
    expressionAttributeNames: TokensToNames?,
    expressionAttributeValues: TokensToValues?
): Item =
    when (updateExpression) {
        null -> this
        else -> updateExpression.split("SET", "REMOVE", "DELETE", "ADD")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .flatMap {
                when {
                    updateExpression.contains("SET $it") -> it.split(",").map { "SET $it" }
                    updateExpression.contains("REMOVE $it") -> it.split(",").map { "REMOVE $it" }
                    updateExpression.contains("DELETE $it") -> it.split(",").map { "DELETE $it" }
                    updateExpression.contains("ADD $it") -> it.split(",").map { "ADD $it" }
                    else -> error("illegal update! $updateExpression")
                }
            }
            .fold(this) { acc, next ->
                when {
                    next.startsWith("SET") ->
                        UpdateSetGrammar.parse(next).eval(
                            ItemWithSubstitutions(
                                acc,
                                expressionAttributeNames ?: emptyMap(),
                                expressionAttributeValues ?: emptyMap()
                            )
                        )
                    next.startsWith("REMOVE") ->
                        UpdateRemoveGrammar.parse(next).eval(
                            ItemWithSubstitutions(
                                acc,
                                expressionAttributeNames ?: emptyMap(),
                                expressionAttributeValues ?: emptyMap()
                            )
                        )
                    next.startsWith("DELETE") ->
                        UpdateRemoveGrammar.parse(next).eval(
                            ItemWithSubstitutions(
                                acc,
                                expressionAttributeNames ?: emptyMap(),
                                expressionAttributeValues ?: emptyMap()
                            )
                        )
                    next.startsWith("ADD") ->
                        UpdateRemoveGrammar.parse(next).eval(
                            ItemWithSubstitutions(
                                acc,
                                expressionAttributeNames ?: emptyMap(),
                                expressionAttributeValues ?: emptyMap()
                            )
                        )
                    else -> error("illegal update! $updateExpression")
                } as Item
            }
    }
