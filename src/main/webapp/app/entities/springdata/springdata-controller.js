/**
 * Created by ferry on 23/06/16.
 */

(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('springdata', springdata);

    springdata.$inject = ['$state','$scope','$http'];
    function springdata ($state,$scope,$http)
    {
        $scope.auteurs = [];
        $http.get("api/auteur/getAllAuthor/auteur")
            .success(function(data){
              console.log(data);
              $scope.auteurs = data;
            })
            .error(function(error)
            {
                console.log(error)
            });
    }
})();

