app.factory("MetaValueManager", function() {
    return {
        setValueByType : function(meta, data) {
            var type = meta.info.type,
                field = meta.field;
            switch(type) {
                case "BigDecimal" : {
                    data[field] = "BigDecimal";
                    break;
                }
            }
        }
    }
});

app.factory("MetaInterceptor", function(MetaValueManager) {


    function MetaResolver(manager) {
        var resolveByType = function(value) {
            if(angular.isObject(value)) {
                iterateObject(value);
            }
            else if(angular.isArray(value)) {
                iterateArray(value);
            }
        }

        function iterateArray(array) {
            angular.forEach(array, function(value) {
                iterateObject(value);
            });
        }

        function iterateObject(data) {
            var meta = data["_meta_"];
            if(meta) {
                angular.forEach(meta, function(value, key) {
                    manager.setValueByType({info: value, field : key}, data);
                });
            }
            angular.forEach(data, function(value, key) {
                if(!angular.equals(key, "_meta_")) {
                    resolveByType(value);
                }
            });

        }

        this.resolve = function(data) {
            resolveByType(data);
            return data;
        }
        return this;
    }

    var metaInterceptor = {
        response: function(response) {
            response.data = new MetaResolver(MetaValueManager).resolve(response.data);
            return response;
        }
    };
    return metaInterceptor;
});


$httpProvider.interceptors.push('MetaInterceptor');

