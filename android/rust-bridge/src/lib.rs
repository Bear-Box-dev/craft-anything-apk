use jni::objects::{JClass, JString};
use jni::sys::jstring;
use jni::JNIEnv;

const BRIDGE_VERSION: &str = "0.1.0";
const CAPABILITIES: &str = r#"{
  "bridge": true,
  "portable_analysis": false,
  "project_storage": false,
  "agent_execution": false,
  "desktop_capture": false,
  "status": "foundation_only"
}"#;

#[unsafe(no_mangle)]
pub extern "system" fn Java_com_bearbox_craftanything_NativeBridge_nativeVersion(
    mut env: JNIEnv,
    _class: JClass,
) -> jstring {
    env.new_string(BRIDGE_VERSION)
        .expect("could not allocate Java version string")
        .into_raw()
}

#[unsafe(no_mangle)]
pub extern "system" fn Java_com_bearbox_craftanything_NativeBridge_nativeCapabilitiesJson(
    mut env: JNIEnv,
    _class: JClass,
) -> jstring {
    env.new_string(CAPABILITIES)
        .expect("could not allocate Java capabilities string")
        .into_raw()
}
