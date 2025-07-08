afterEvaluate {
    gradle.taskGraph.whenReady {
        allTasks.forEach {
            if (it.name.startsWith("merge") && it.name.endsWith("NativeLibs")) {
                logger.quiet("Found task: ${it.path}")
                it.doFirst {
                    inputs.files.files.forEach { f ->
                        if (f.isDirectory) {
                            val so = f.walkBottomUp().filter { s -> s.isFile && s.name.endsWith(".so") }
                                .map { it.relativeTo(f) }.toList()
                            logger.lifecycle("NativeLibs: inputs = ${f.absoluteFile}, so = $so")
                        } else {
                            logger.lifecycle("NativeLibs: inputs = ${f.absoluteFile}")
                        }
                    }
                }
            }
        }
    }
}