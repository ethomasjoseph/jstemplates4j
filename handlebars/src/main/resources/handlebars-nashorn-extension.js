if (typeof hbwrapper === "undefined") {
	var hbwrapper = {};

	(function(context) {
		/**
		 * Proxy function to register a helper written in Java to Handlebars
		 * engine.
		 * 
		 * @param name
		 *            the name of the helper.
		 * @param helper
		 *            the actual Java helper function which will be invoked.
		 */
		context.registerHelper = function(name, helper) {
			Handlebars.registerHelper(name, function(context, options){
				var Options = Java.type('com.ethomasjoseph.jstemplates4j.handlebars.compiler.helper.Options');
				var newOptions = new Options(options);
				return helper.apply(context, newOptions);
			});
		},

		/**
		 * Wrapper function to wrap existing helpers to be compatible to Java
		 * world. The original helper functions are proxied here, after
		 * performing necessary sanitization.
		 */
		wrapExistingHelpers = function() {
			var helpersToWrap = ["blockHelperMissing", "helperMissing", "each", "if", "unless", "with", "log", "lookup"];
			var currentHelpers = Handlebars.helpers;
			var originalHelpers = {};
			
			// cache the original helpers
			for (var i in currentHelpers) {
				originalHelpers[i] = currentHelpers[i];
			}
			
			// now wrap the helpers required
			for(i in helpersToWrap) {
				var helper = helpersToWrap[i];
				var fn = originalHelpers[helper];
				if (typeof fn === 'function') {
					Handlebars.unregisterHelper(helper);
					Handlebars.registerHelper(helper, function(context, options) {
//						if (typeof options === "undefined") {
//							return;
//						}
						var fn = originalHelpers[options.name];
						if (typeof fn == "undefined") {
							// making it mustache compatible, assuming that the helper called is actually a data to be iterated on
							fn = originalHelpers["each"];
						}
						return fn.call(this, safeContext(context), options);
					});
				}
			}
		},
		
		/**
		 * Sanitizes the given context object, which may be a Java object, so
		 * that it is consumable by JavaScript.
		 * 
		 * @param context
		 *            the context / data that needs to be made safe.
		 * @returns the safe context object that can be consumed by JavaScript
		 *          world.
		 */
		safeContext = function(context) {
			if(context instanceof Java.type("java.util.List")) {
				return Java.from(context);
			}
			return context;
		};

		// auto-execute functions
		wrapExistingHelpers();
	})(hbwrapper);
}
