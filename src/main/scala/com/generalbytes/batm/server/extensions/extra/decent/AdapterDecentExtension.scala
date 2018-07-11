package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.ExtensionAdapter

// WARN: DON'T change the name. Name must end with the word "Extension", because GeneralBytes' API is retarded like that.
class AdapterDecentExtension extends ExtensionAdapter(new DecentExtension())
