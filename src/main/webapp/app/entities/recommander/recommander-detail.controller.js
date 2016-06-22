(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('RecommanderDetailController', RecommanderDetailController);

    RecommanderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Recommander', 'Ouvrage', 'Utilisateur'];

    function RecommanderDetailController($scope, $rootScope, $stateParams, entity, Recommander, Ouvrage, Utilisateur) {
        var vm = this;

        vm.recommander = entity;

        var unsubscribe = $rootScope.$on('projetApp:recommanderUpdate', function(event, result) {
            vm.recommander = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
