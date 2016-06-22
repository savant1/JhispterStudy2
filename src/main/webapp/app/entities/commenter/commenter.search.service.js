(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('CommenterSearch', CommenterSearch);

    CommenterSearch.$inject = ['$resource'];

    function CommenterSearch($resource) {
        var resourceUrl =  'api/_search/commenters/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
