(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('CommenterDetailController', CommenterDetailController);

    CommenterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Commenter', 'Ouvrage', 'Utilisateur'];

    function CommenterDetailController($scope, $rootScope, $stateParams, entity, Commenter, Ouvrage, Utilisateur) {
        var vm = this;

        vm.commenter = entity;

        var unsubscribe = $rootScope.$on('projetApp:commenterUpdate', function(event, result) {
            vm.commenter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
