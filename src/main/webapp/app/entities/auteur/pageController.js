(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('PageController', PageController);

    PageController.$inject = ['$state','$scope','$http'];

    function PageController ($state,$scope,$http)
    {
        $scope.valide = function()
        {
            $http.get("http://localhost:8080/api/auteur/concat/"+$scope.nom+"/"+$scope.prenom).success(function(data){
                console.log(data);
                $scope.reponse = data;
            }).error(function(error)
            {
                console.log(error)
            });
            console.log($scope.nom);
            //$state.go('Reponse');
        }
    }
})();
