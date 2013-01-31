

less.Parser.importer = function(path, paths, fn) {
    if (path != null) {
      //java.lang.System.out.println("Importing " + path);
      //Delegate the data reading to the URILocator
      var data = resourceReader.readFile(path);
      new (less.Parser)({
        optimization : 3,
        paths : [ String(path).replace(/[\w\.-]+$/, '') ]
      }).parse(String(data).replace(/\r/g, ''), function(e,
          root) {
        if (e instanceof Object)
          throw e;
        fn(e, root);
        if (e instanceof Object)
          throw e;
      });
    }
  };



var lessIt = function(css) {

  var result;
  var parser = new less.Parser({
    optimization : 2
  });



  parser.parse(css, function(e, root) {
    if (e) {
      throw e;
    }
    result = css;
    result = root.toCSS();
  });
  return result;
};
