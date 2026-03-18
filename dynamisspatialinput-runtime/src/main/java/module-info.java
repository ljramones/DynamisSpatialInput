module org.dynamisengine.spatialinput.runtime {
    requires transitive org.dynamisengine.spatialinput.api;
    requires org.dynamisengine.spatialinput.core;

    exports org.dynamisengine.spatialinput.runtime;

    uses org.dynamisengine.spatialinput.runtime.SpatialBackend;
}
