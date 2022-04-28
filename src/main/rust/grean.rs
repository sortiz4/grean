use std::env;
use std::fs;
use std::process::Command;

fn main() {
    // Get the module path
    let mut module_path = match env::current_exe() {
        Ok(p) => p.to_path_buf(),
        _ => return,
    };
    module_path.pop();
    module_path.pop();
    module_path.push("lib");

    // Construct the class path
    let class_path = {
        fs::read_dir(&module_path)
            .unwrap()
            .map(|entry| entry.unwrap().path().to_str().unwrap().to_owned())
            .collect::<Vec<_>>()
            .join(if cfg![target_os = "windows"] { ";" } else { ":" })
    };

    // Construct the arguments
    let default_arguments = &[
        "--module-path", module_path.to_str().unwrap(),
        "-classpath", &class_path,
        "grean.Main",
    ];
    let merged_arguments = {
        default_arguments
            .iter()
            .map(|string| string.to_string())
            .chain(env::args().skip(1).map(|string| string.to_string()))
    };

    // Spawn the Java process
    Command::new("java")
        .args(merged_arguments)
        .status()
        .unwrap();
}
