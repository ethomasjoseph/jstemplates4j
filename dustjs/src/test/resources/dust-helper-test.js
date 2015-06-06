if (typeof dusthelper === "undefined") {
	var dusthelper = {
		"testhelper" : function(chunk, context, bodies, params) {
			print("registering helper testhelper");
		},
		
		"testhelper2" : function(chunk, context, bodies, params) {
			print("registering helper testhelper2");
		}
	};
}
