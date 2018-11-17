package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.adapters.ExtensionAdapter
import com.generalbytes.batm.common.domain.Task
import com.generalbytes.batm.server.extensions.extra.decent.extension.DecentExtension

// WARN: DON'T change the name. Name must end with the word "Extension", because GeneralBytes' API is retarded
// and looks for extensions by suffix "Extension" (yes, really, I'm not kidding).
class AdapterDecentExtension extends ExtensionAdapter[Task](new DecentExtension())
