include_defs('//bucklets/gerrit_plugin.bucklet')

gerrit_plugin(
  name = 'login-redirect',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/resources/**/*']),
  manifest_entries = [
    'Gerrit-PluginName: login-redirect',
    'Gerrit-HttpModule: com.googlesource.gerrit.plugins.loginredirect.LoginRedirectModule',
    'Implementation-Title: Login Redirect plug-in',
  ]
)

java_library(
  name = 'classpath',
  deps = GERRIT_PLUGIN_API + [':login-redirect__plugin'],
)
