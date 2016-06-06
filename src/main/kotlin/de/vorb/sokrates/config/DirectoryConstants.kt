package de.vorb.sokrates.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DirectoryConstants @Autowired constructor(
        private val dirProperties: DirProperties)  {



}
