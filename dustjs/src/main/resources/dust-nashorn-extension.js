if (typeof dustwrapper === "undefined") {
	var dustwrapper = {};

	(function(context) {
		context.render = function(jsTemplate, data) {
			var RenderedOutput = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.internal.RenderedOutput');
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
		
		/**
		 * Proxy function to register a helper written in Java to Dust
		 * engine.
		 * 
		 * @param name
		 *            the name of the helper.
		 * @param helper
		 *            the actual Java helper whose function which will be invoked.
		 */
		context.registerHelper = function(name, helper) {
			dust.helpers[name] = function(chunk, context, bodies, params) {
				/* Declaring Java Types as JS variables */
				var Chunk = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.Chunk');
				var Context = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.Context');
				var Bodies = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.Bodies');
				var Params = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.Params');
				
				/* Declaring the new variables */
				var wrappedChunk = new Chunk(chunk);
				var wrappedContext = new Context(context);
				var wrappedBodies = new Bodies(bodies);
				
				var map = new Params();
				if (typeof params != "undefined") {
					for(var key in params) {
						map.put(key, params[key]);
					}
				}
				// invoke the function on the Java helper
				var result = helper.apply(wrappedChunk, wrappedContext, wrappedBodies, map);
				return result.mirror;
			}
		},
		
		context.unregisterHelper = function(name) {
			dust.helpers[name] = undefined;
		}
		
	})(dustwrapper);
}
