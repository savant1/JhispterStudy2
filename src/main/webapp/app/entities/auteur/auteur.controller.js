(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('AuteurController', AuteurController);

    AuteurController.$inject = ['$scope', '$state', 'Auteur', 'AuteurSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','$http'];

    function AuteurController ($scope, $state, Auteur, AuteurSearch, ParseLinks, AlertService, pagingParams, paginationConstants, $http) {
        var vm = this;
        vm.auteurNewAttribute = [];
        $scope.objet = {id:"", nom:"", prenom: "", nombreOuv:""};

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;

        loadAll();

        function loadAll () {
            if (pagingParams.search) {
                AuteurSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Auteur.query({
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.auteurs = data;
                vm.page = pagingParams.page;
                for(var nn=0; nn<vm.auteurs.length; nn++){
                    $scope.objet.id = vm.auteurs[nn].id;
                    $scope.objet.nom = vm.auteurs[nn].nom;
                    $scope.objet.prenom = vm.auteurs[nn].prenom;
                    $scope.objet.nombreOuv = 0;

                    $scope.init($scope.objet.id);

                    vm.auteurNewAttribute.push($scope.objet);
                    $scope.objet = {};

                };
              //  $scope.init();
            }


            $scope.init = function (id){
                console.log(vm.auteurNewAttribute[3]);
              //  for(var kk = 0; kk<vm.auteurNewAttribute.length; kk++){
                    $http.get("/api/auteur/countOuvrage/"+id)
                        .success(function(resp){
                            console.log(resp);
                            vm.auteurNewAttribute[id-1].nombreOuv = resp;
                        })
                        .error(function(error)
                        {
                            console.log(error)
                        });
             //   }
            };


            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear () {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
    }
})();
