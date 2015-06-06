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
		
		context.registerHelper = function(helpers, names) {
			print(JSON.stringify(helpers));
			if (typeof names === "undefined") {
				for(var key in helpers) {
					// register all the helpers in the namespace
					print("key1 : " + key);
					dust.helpers[key] = helpers[key];
				}
			} else {
				for(var key in names) {
					// selectively register the helpers whose names are specified
					var helper = helpers[key];
					print("helper2 = " + helper);
					if (typeof helper != "undefined") {
						dust.helpers[key] = helper;
					}
				}
			}
		},
		
//		context.registerHelper = function(name, helper) {
//			dust.helpers[name] = function(chunk, context, bodies, params){
//				var dChunk = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.Chunk');
//				var dBodies = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.Bodies');
//				var dParams = Java.type('com.ethomasjoseph.jstemplates4j.dustjs.compiler.helper.Params');
//				
//				print(chunk);
////				print(context);
////				print(bodies);
////				print(params);
//
////				print(JSON.stringify(chunk));
//				print(JSON.stringify(context));// the data
//				print(JSON.stringify(bodies)); // the function of block statements
//				print("[" + bodies.block + " | " + bodies.else + "]");
//				print(JSON.stringify(params));
//				
//				var retChunk = helper.apply(new dChunk(chunk), new Context(context), new Bodies(bodies), new Params(params));
//				return retChunk.native;
//			};
//		},
		
		context.unregisterHelper = function(name) {
			dust.helpers[name] = undefined;
		}
		
	})(dustwrapper);
}
