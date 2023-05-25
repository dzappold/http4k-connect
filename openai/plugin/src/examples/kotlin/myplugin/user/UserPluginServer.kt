package myplugin.user

import myplugin.user.UserPluginSettings.PORT
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.server.SunHttp
import org.http4k.server.asServer

/**
 * Binds the Plugin to a server and starts it as a JVM app
 */
fun UserPluginServer(env: Environment = ENV) = UserPlugin(env).asServer(SunHttp(PORT(env)))

fun main() {
    UserPluginServer().start()
}
