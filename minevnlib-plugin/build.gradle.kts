plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":minevnlib-bukkit"))
    implementation(project(":minevnlib-bungee"))

    // database
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.flywaydb:flyway-core:9.22.0")
    implementation("com.h2database:h2:2.1.214")
}

tasks {
    val jarName = "MineVNLib"

    register("customCopy") {
        dependsOn(shadowJar)

        val path = project.properties["shadowPath"]
        if (path != null) {
            doLast {
                println(path)
                copy {
                    from("build/libs/$jarName.jar")
                    into(path)
                }
                println("Copied")
            }
        }
    }

    shadowJar {
        // relocate to avoid conflict
        relocate("com.zaxxer.hikari", "net.minevn.libs.hikari")
        relocate("com.fasterxml.jackson", "net.minevn.libs.jackson")
        relocate("com.google.gson", "net.minevn.libs.gson")
        relocate("org.slf4j", "net.minevn.libs.slf4j")
        archiveFileName.set("$jarName.jar")
    }

    assemble {
        dependsOn(shadowJar, get("customCopy"))
    }
}
