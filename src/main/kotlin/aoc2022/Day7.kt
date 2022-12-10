package aoc2022

object Day7 : AOC {
    private fun String.cmd() = when {
        startsWith("\$ cd") -> CMD.CD
        startsWith("\$ ls") -> CMD.LS
        else -> null
    }
    enum class CMD { CD, LS }
    object FS {
        val size = 70000000
        val root = Dir("/", parent = null)
        fun free() = size - root.size()
        fun print() = root.print()
    }

    data class File(val name: String, val size: Int, val parent: Dir) {
        private val level: Int = parent.level.plus(1)
        override fun toString(): String = "$name (file, size=$size)"
        fun print() {
            repeat(level) {
                print("\t")
            }
            print("- $this\n")
        }
    }

    data class Dir(val name: String, val parent: Dir?, val files: MutableList<File> = mutableListOf(), val dirs: MutableList<Dir> = mutableListOf()) {
        val level: Int = parent?.level?.plus(1) ?: 0
        override fun toString(): String = "$name (dir, size=${size()})"
        fun size() : Int = files.sumOf { it.size } + dirs.sumOf { it.size() }
        fun rDirs() : List<Dir> = dirs + dirs.flatMap { it.rDirs() }

        fun print() {
            repeat(level) {
                print("\t")
            }
            print("- $this\n")
            dirs.forEach(Dir::print)
            files.forEach(File::print)
        }
    }

    override fun solve(): String {
        var cwd = FS.root
        "day7.input".read().lines()
            .filter { it.isNotEmpty() }
            .forEachIndexed { _, it ->
                when (it.cmd()) {
                    CMD.CD -> {
                        val dirName = it.split(" ").last()
                        if (dirName == "..") {
                            cwd = cwd.parent ?: FS.root
                        } else if (dirName == "/") {
                            cwd = FS.root
                        } else {
                            if (dirName != cwd.name) {
                                cwd = cwd.dirs.find { d -> d.name == dirName }
                                    ?: Dir(name = dirName, parent = cwd).apply {
                                        cwd.dirs.add(this)
                                    }
                            }
                        }
                    }
                    CMD.LS -> Unit // noop, contents next
                    null -> { // contents
                        if (it.startsWith("dir")) {
                            val dirName = it.split(" ").last()
                            cwd.dirs.add(Dir(name = dirName, parent = cwd))
                        } else {
                            val (size, name) = it.split(" ")
                            cwd.files.add(File(name = name, size = size.toInt(), parent = cwd))
                        }
                    }
                }
            }

        // FS.aoc2022.print() // debug
        val minFree = 30000000
        val need = minFree - FS.free()
        return """
            |## Day 7
            |* Part 1: 
            | * ${FS.root.rDirs().filter { it.size() < 100000 }.sumOf { it.size() }}
            |* Part 2: 
            | * ${FS.root.rDirs().filter { it.size() > need }.minByOrNull { it.size() }}
        """.trimMargin()
    }
}