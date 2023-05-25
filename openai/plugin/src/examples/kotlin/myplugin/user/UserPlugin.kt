package myplugin.user

import myplugin.shared.UserDirectory
import myplugin.user.UserPluginSettings.EMAIL
import myplugin.user.UserPluginSettings.PLUGIN_BASE_URL
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.connect.openai.auth.AuthToken.Basic
import org.http4k.connect.openai.auth.UserLevelAuth
import org.http4k.connect.openai.info
import org.http4k.connect.openai.openAiPlugin
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters.InitialiseRequestContext
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.routing.RoutingHttpHandler

/**
 * Main creation pattern for an OpenAI plugin
 */
fun UserPlugin(env: Environment = ENV): RoutingHttpHandler {
    val userDirectory = UserDirectory()
    val contexts = RequestContexts()
    val userPrincipal = RequestContextKey.required<UserId>(contexts)

    return InitialiseRequestContext(contexts)
        .then(
            openAiPlugin(
                info(
                    apiVersion = "1.0",
                    humanDescription = "userplugin" to "A plugin which uses user-level auth",
                    pluginUrl = PLUGIN_BASE_URL(env),
                    contactEmail = EMAIL(env),
                ),
                UserLevelAuth(userDirectory.authUser(userPrincipal)),
                GetMyAddress(userPrincipal, userDirectory)
            )
        )
}

/**
 * Populate a known user if their password matches
 */
private fun UserDirectory.authUser(userPrincipal: RequestContextLens<UserId>) =
    Basic("realm", userPrincipal) { credentials ->
        UserId.of(credentials.user)
            .takeIf { auth(credentials) }
    }

