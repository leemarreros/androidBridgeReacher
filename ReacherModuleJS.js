var {
  NativeModules: {
    ReacherModule
  }
} = require('react-native');

var ReacherModuleJS = {

  reach: function(input, callback) {

    return new Promise((resolve, reject) => {

      ReacherModule.reach(input, (err) => {
        callback && callback(err, null);
        reject(err);
      }, (data) => {
        callback && callback(null, data);
        resolve(data);
      });
    });
  }
};


module.exports = ReacherModuleJS;