if (typeof dustwrapper === "undefined") {
	var dustwrapper = {};

	(function(context) {
		context.render = function(jsTemplate, data) {
			var RenderedOutput = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.internal.RenderedOutput');
			var rendered;
			dust.render(jsTemplate.name, data, function(err, out) {
				rendered = new RenderedOutput(err, out);
			});
			return rendered;
		},
		
		context.registerJSHelper = function(helpers, names) {
			if (typeof names === "undefined" || names === null || names.length === 0) {
				for(var key in helpers) {
					// register all the helpers in the namespace
					dust.helpers[key] = helpers[key];
				}
			} else {
				for(var key in names) {
					// selectively register the helpers whose names are specified
					var helper = names[key];
					var selectedHelper = helpers[helper];
					if (typeof selectedHelper != "undefined") {
						dust.helpers[helper] = selectedHelper;
					}
				}
			}
			return dust;
		},
		
		context.unregisterHelper = function(name) {
			dust.helpers[name] = undefined;
		}
		
	})(dustwrapper);
}
