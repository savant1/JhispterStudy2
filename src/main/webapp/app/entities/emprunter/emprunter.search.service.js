(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('EmprunterSearch', EmprunterSearch);

    EmprunterSearch.$inject = ['$resource'];

    function EmprunterSearch($resource) {
        var resourceUrl =  'api/_search/emprunters/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
