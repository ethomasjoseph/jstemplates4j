if (typeof dusthelper === "undefined") {
	var dusthelper = {
		"testhelper" : function(chunk, context, bodies, params) {
			return chunk.write(" |this is helper1| ");
		},
		
		"testhelper2" : function(chunk, context, bodies, params) {
			return chunk.write(" |THIS IS HELPER2| ");
		}
	};
	
	for(var key in dusthelper) {
		dust.helpers[key] = dusthelper[key];
	}
}
