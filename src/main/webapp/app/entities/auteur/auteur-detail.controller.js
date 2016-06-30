(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('AuteurDetailController', AuteurDetailController);

    AuteurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Auteur', 'Realiser','$http'];

    function AuteurDetailController($scope, $rootScope, $stateParams, entity, Auteur, Realiser, $http) {
        var vm = this;

        vm.auteur = entity;

        var unsubscribe = $rootScope.$on('projetApp:auteurUpdate', function(event, result) {
            vm.auteur = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function getNbreOuvrage(){
            $http.get("api/auteur/getNbreOuvrageBy/"+vm.auteur.nom)
                .success(function(data){
                    $scope.nbreOuv = data;
                })
                .error(function(error)
                {
                    console.log(error)
                });
        }
        getNbreOuvrage();
    }
})();
