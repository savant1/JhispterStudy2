(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('RealiserDialogController', RealiserDialogController);

    RealiserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Realiser', 'Ouvrage', 'Auteur'];

    function RealiserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Realiser, Ouvrage, Auteur) {
        var vm = this;

        vm.realiser = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.ouvrages = Ouvrage.query({filter: 'realiser-is-null'});
        $q.all([vm.realiser.$promise, vm.ouvrages.$promise]).then(function() {
            if (!vm.realiser.ouvrage || !vm.realiser.ouvrage.id) {
                return $q.reject();
            }
            return Ouvrage.get({id : vm.realiser.ouvrage.id}).$promise;
        }).then(function(ouvrage) {
            vm.ouvrages.push(ouvrage);
        });
        vm.auteurs = Auteur.query({filter: 'realiser-is-null'});
        $q.all([vm.realiser.$promise, vm.auteurs.$promise]).then(function() {
            if (!vm.realiser.auteur || !vm.realiser.auteur.id) {
                return $q.reject();
            }
            return Auteur.get({id : vm.realiser.auteur.id}).$promise;
        }).then(function(auteur) {
            vm.auteurs.push(auteur);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.realiser.id !== null) {
                Realiser.update(vm.realiser, onSaveSuccess, onSaveError);
            } else {
                Realiser.save(vm.realiser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:realiserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
