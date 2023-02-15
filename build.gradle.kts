import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	application
	kotlin("jvm") version "1.7.10"
	kotlin("plugin.spring") version "1.6.21"
	id("org.springframework.boot") version "2.7.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("io.gitlab.arturbosch.detekt") version "1.19.0"
	id("org.openapi.generator") version "5.1.1"
	id("jacoco")
}

val mainPkgAndClass = "com.kaiqkt.services.communicationservice.ApplicationKt"

val excludePackages: List<String> by extra {
	listOf(
		"com/kaiqkt/services/communicationservice/Application*",
		"com/kaiqkt/services/communicationservice/generated/application/controllers/**",
		"com/kaiqkt/services/communicationservice/generated/application/dto/**",
		"com/kaiqkt/services/communicationservice/application/dto/**",
		"com/kaiqkt/services/communicationservice/application/ext/**",
		"com/kaiqkt/services/communicationservice/application/handler/WebSocketHandler**",
		"com/kaiqkt/services/communicationservice/resources/swagger/**",
		"com/kaiqkt/services/communicationservice/domain/entities/**",
		"com/kaiqkt/services/communicationservice/domain/exceptions/**",
		"com/kaiqkt/services/communicationservice/resources/aws/sqs/JmsConfig**",
		"com/kaiqkt/services/communicationservice/resources/aws/sqs/AmazonSQSConfig**",
		"com/kaiqkt/services/communicationservice/resources/aws/s3/AmazonS3Config**",
		"com/kaiqkt/services/communicationservice/resources/exceptions/**",
		"com/kaiqkt/services/communicationservice/resources/websocket/config/**",
		"com/kaiqkt/services/communicationservice/resources/cache/RedisConfig**",
	)
}

@Suppress("UNCHECKED_CAST")
fun ignorePackagesForReport(jacocoBase: JacocoReportBase) {
	jacocoBase.classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(jacocoBase.project.extra.get("excludePackages") as List<String>)
		}
	)
}

application {
	mainClass.set(mainPkgAndClass)
}

group = "com.kaiqkt.services"
version = "1.0.0"

repositories {
	mavenCentral()
	maven {
		name = "GitHubPackages"
		url = uri("https://maven.pkg.github.com/kaiqkt/*")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
			password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
		}
	}
}

sourceSets {
	create("componentTest") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}
}

val componentTestImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

configurations["componentTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

val componentTestTask = tasks.create("componentTest", Test::class) {
	description = "Runs the component tests."
	group = "verification"

	testClassesDirs = sourceSets["componentTest"].output.classesDirs
	classpath = sourceSets["componentTest"].runtimeClasspath

	useJUnitPlatform()
}

java.sourceSets["main"].java.srcDir("$buildDir/generated/src/main/kotlin")

configurations.implementation {
	exclude("org.springframework.boot", "spring-boot-starter-logging")
}

dependencies {
	//kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	//commons
	implementation("com.kaiqkt.commons:commons-security:1.3.0")
	implementation("com.kaiqkt.commons:commons-health:1.0.0")
	implementation("com.kaiqkt.commons:commons-crypto:1.0.2")

	//swagger
	implementation("org.springdoc:springdoc-openapi-ui:1.6.14")

	//spring
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-messaging")
	implementation("org.springframework:spring-jms:5.3.21")
	implementation("org.springframework.boot:spring-boot-starter-websocket")

	//database
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("redis.clients:jedis:3.8.0")

	//logging
	implementation("org.slf4j:slf4j-api")
	implementation("org.slf4j:slf4j-simple")

	//aws
	implementation("com.amazonaws:aws-java-sdk:1.12.300")
	implementation("com.amazonaws:amazon-sqs-java-messaging-lib:1.0.4")

	//fuel
	implementation("com.github.kittinunf.fuel:fuel:2.3.1")
	implementation("io.azam.ulidj:ulidj:1.0.1")

	//utils
	implementation("org.apache.commons:commons-text:1.9")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	testImplementation("com.ninja-squad:springmockk:3.1.1")
	testImplementation("com.icegreen:greenmail-junit5:1.6.1")
	testImplementation( "org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mock-server:mockserver-netty:5.11.2")

	componentTestImplementation("org.elasticmq:elasticmq-rest-sqs_2.12:0.15.8")
	componentTestImplementation("io.findify:s3mock_2.12:0.2.5")
	componentTestImplementation("it.ozimov:embedded-redis:0.7.3")
	componentTestImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.4.8")
	componentTestImplementation("org.awaitility:awaitility-kotlin:4.2.0")
	componentTestImplementation("org.springframework.boot:spring-boot-starter-test")
	componentTestImplementation("org.mock-server:mockserver-netty:5.11.2")
	componentTestImplementation("com.icegreen:greenmail-junit5:1.6.1")
	componentTestImplementation(sourceSets["test"].output)
}

jacoco {
	toolVersion = "0.8.7"
	reportsDirectory.set(layout.buildDirectory.dir("jacoco"))
}

detekt {
	source = files("src/main/java", "src/main/kotlin")
	config = files("detekt/detekt.yml")
}

openApiGenerate {
	generatorName.set("kotlin-spring")
	skipValidateSpec.set(true)
	configFile.set("$rootDir/src/main/resources/static/api-config.json")
	inputSpec.set("$rootDir/src/main/resources/static/api-docs.yml")
	outputDir.set("$buildDir/generated/") }

tasks.withType<KotlinCompile> {
	java.sourceCompatibility = JavaVersion.VERSION_11
	java.targetCompatibility = JavaVersion.VERSION_11

	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

springBoot {
	buildInfo()
}

tasks.withType<CreateStartScripts> { mainClass.set(mainPkgAndClass) }


tasks.jar {
	isZip64 = true
	enabled = false
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	manifest {
		attributes("Main-Class" to mainPkgAndClass)
		attributes("Package-Version" to archiveVersion)
	}

	from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
	from(sourceSets.main.get().output)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<JacocoReport> {
	reports {
		xml.required
		html.required
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
	ignorePackagesForReport(this)
}

tasks.withType<JacocoCoverageVerification> {
	violationRules {
		rule {
			limit {
				minimum = "1.0".toBigDecimal()
				counter = "LINE"
			}
			limit {
				minimum = "1.0".toBigDecimal()
				counter = "BRANCH"
			}
		}
	}
	ignorePackagesForReport(this)
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification, componentTestTask)
}
