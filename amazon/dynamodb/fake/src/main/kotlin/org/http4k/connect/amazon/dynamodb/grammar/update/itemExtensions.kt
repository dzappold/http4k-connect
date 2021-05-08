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
            .fold(this) { acc, next ->
                when {
                    updateExpression.contains("SET $next") ->
                        UpdateSetGrammar.parse(next).eval(
                            ItemWithSubstitutions(
                                acc,
                                expressionAttributeNames ?: emptyMap(),
                                expressionAttributeValues ?: emptyMap()
                            )
                        )
                    updateExpression.contains("REMOVE $next") ->
                        UpdateRemoveGrammar.parse(next).eval(
                            ItemWithSubstitutions(
                                acc,
                                expressionAttributeNames ?: emptyMap(),
                                expressionAttributeValues ?: emptyMap()
                            )
                        )
                    updateExpression.contains("DELETE $next") ->
                        UpdateRemoveGrammar.parse(next).eval(
                            ItemWithSubstitutions(
                                acc,
                                expressionAttributeNames ?: emptyMap(),
                                expressionAttributeValues ?: emptyMap()
                            )
                        )
                    updateExpression.contains("ADD $next") ->
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
