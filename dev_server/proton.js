require('./proton-dev.js');
module.exports = require('./out_dev/proton/core.js');
// hack to enable eval and prevent CSP error in chrome
// check https://github.com/atom/loophole for more info
global.eval = function(content) {
  require('vm').runInThisContext(content);
}
