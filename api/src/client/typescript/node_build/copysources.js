"use strict";
exports.__esModule = true;
/**
 * this is a small script
 * which handles the recursive copy, it atm uses the fs-extra module which is under mit license
 * for build purposes.
 *
 * This module can be replaced with custom code if needed.
 *
 */
var fse = require("fs-extra");
var path = require("path");
var cwd = process.cwd();
/**
 * a small shim over the fsextra call
 * to recursively copy a directory
 * @param source
 * @param target
 */
function copyFilesRecursively(source, target) {
    fse.copySync(path.resolve(cwd, source), path.resolve(cwd, target), { overwrite: true });
    console.log("".concat(source, " sucessfully copied to ").concat(target));
}
/**
 * the files which need to be copied in our case the generated sources and docs (for now we do not host
 * the files themselves)
 *
 * We are simply relocating to the old location of the files
 * standard jsf namespace and myfaces for the extended namespaces
 *
 * TODO we need to find a way to merge the jsdocs since we have to retire our existing
 * assembly plugin for jsdocs
 */
copyFilesRecursively('./node_modules/jsf.js_next_gen/src/main/typescript', './typescript/jsf_ts');
copyFilesRecursively('./node_modules/jsf.js_next_gen/src/main/types', './typescript/types');
copyFilesRecursively('./node_modules/mona-dish/src/main/typescript', './typescript/mona_dish');
//# sourceMappingURL=copysources.js.map