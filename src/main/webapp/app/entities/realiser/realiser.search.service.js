(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('RealiserSearch', RealiserSearch);

    RealiserSearch.$inject = ['$resource'];

    function RealiserSearch($resource) {
        var resourceUrl =  'api/_search/realisers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
