(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('CommenterDialogController', CommenterDialogController);

    CommenterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Commenter', 'Ouvrage', 'Utilisateur'];

    function CommenterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Commenter, Ouvrage, Utilisateur) {
        var vm = this;

        vm.commenter = entity;
        vm.clear = clear;
        vm.save = save;
        vm.ouvrages = Ouvrage.query({filter: 'commenter-is-null'});
        $q.all([vm.commenter.$promise, vm.ouvrages.$promise]).then(function() {
            if (!vm.commenter.ouvrage || !vm.commenter.ouvrage.id) {
                return $q.reject();
            }
            return Ouvrage.get({id : vm.commenter.ouvrage.id}).$promise;
        }).then(function(ouvrage) {
            vm.ouvrages.push(ouvrage);
        });
        vm.utilisateurs = Utilisateur.query({filter: 'commenter-is-null'});
        $q.all([vm.commenter.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.commenter.utilisateur || !vm.commenter.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.commenter.utilisateur.id}).$promise;
        }).then(function(utilisateur) {
            vm.utilisateurs.push(utilisateur);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.commenter.id !== null) {
                Commenter.update(vm.commenter, onSaveSuccess, onSaveError);
            } else {
                Commenter.save(vm.commenter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:commenterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
