/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.afyastat.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Person;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.afyastat.api.db.ArchiveInfoDao;
import org.openmrs.module.afyastat.api.db.ErrorInfoDao;
import org.openmrs.module.afyastat.api.db.NotificationInfoDao;
import org.openmrs.module.afyastat.api.db.AfyaDataSourceDao;
import org.openmrs.module.afyastat.api.db.AfyaStatQueueDataDao;
import org.openmrs.module.afyastat.api.db.ErrorMessagesInfoDao;
import org.openmrs.module.afyastat.api.service.InfoService;
import org.openmrs.module.afyastat.api.service.RegistrationInfoService;
import org.openmrs.module.afyastat.exception.StreamProcessorException;
import org.openmrs.module.afyastat.model.*;
import org.openmrs.module.afyastat.model.ErrorInfo;
import org.openmrs.module.afyastat.model.handler.QueueInfoHandler;
import org.openmrs.util.HandlerUtil;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class InfoServiceImpl extends BaseOpenmrsService implements InfoService {
	
	private ErrorInfoDao errorInfoDao;
	
	private AfyaStatQueueDataDao afyaStatQueueDataDao;
	
	private ArchiveInfoDao archiveInfoDao;
	
	private AfyaDataSourceDao afyaDataSourceDao;
	
	private NotificationInfoDao notificationInfoDao;
	
	private ErrorMessagesInfoDao errorMessagesInfoDao;
	
	public AfyaStatQueueDataDao getAfyaStatQueueDataDao() {
		return afyaStatQueueDataDao;
	}
	
	public void setAfyaStatQueueDataDao(final AfyaStatQueueDataDao afyaStatQueueDataDao) {
		this.afyaStatQueueDataDao = afyaStatQueueDataDao;
	}
	
	public ErrorInfoDao getErrorInfoDao() {
		return errorInfoDao;
	}
	
	public void setErrorInfoDao(final ErrorInfoDao errorDataDao) {
		this.errorInfoDao = errorDataDao;
	}
	
	public ArchiveInfoDao getArchiveInfoDao() {
		return archiveInfoDao;
	}
	
	public void setArchiveInfoDao(final ArchiveInfoDao archiveInfoDao) {
		this.archiveInfoDao = archiveInfoDao;
	}
	
	public AfyaDataSourceDao getAfyaDataSourceDao() {
		return afyaDataSourceDao;
	}
	
	public void setAfyaDataSourceDao(final AfyaDataSourceDao afyaDataSourceDao) {
		this.afyaDataSourceDao = afyaDataSourceDao;
	}
	
	public NotificationInfoDao getNotificationInfoDao() {
		return notificationInfoDao;
	}
	
	public void setNotificationInfoDao(final NotificationInfoDao notificationDataDao) {
		this.notificationInfoDao = notificationDataDao;
	}
	
	public ErrorMessagesInfoDao getErrorMessagesInfoDao() {
		return errorMessagesInfoDao;
	}
	
	public void setErrorMessagesInfoDao(final ErrorMessagesInfoDao errorMessagesInfoDao) {
		this.errorMessagesInfoDao = errorMessagesInfoDao;
	}
	
	/**
	 * Return the data with the given id.
	 * 
	 * @param id the form data id.
	 * @return the form data with the matching id.
	 * @should return form data with matching id.
	 * @should return null when no form data with matching id.
	 */
	@Override
	public AfyaStatQueueData getQueueData(final Integer id) {
		return getAfyaStatQueueDataDao().getData(id);
	}
	
	/**
	 * Return the data with the given uuid.
	 * 
	 * @param uuid the form data uuid.
	 * @return the form data with the matching uuid.
	 * @should return form data with matching uuid.
	 * @should return null when no form data with matching uuid.
	 */
	@Override
	public AfyaStatQueueData getQueueDataByUuid(final String uuid) {
		return getAfyaStatQueueDataDao().getDataByUuid(uuid);
	}
	
	/**
	 * Return all saved form data.
	 * 
	 * @return all saved form data.
	 * @should return empty list when no form data are saved in the database.
	 * @should return all saved form data.
	 */
	@Override
	public List<AfyaStatQueueData> getAllQueueData() {
		return getAfyaStatQueueDataDao().getAllData();
	}
	
	/**
	 * Save form data into the database.
	 * 
	 * @param formData the form data.
	 * @return saved form data.
	 * @should save form data into the database.
	 */
	@Override
	public AfyaStatQueueData saveQueueData(final AfyaStatQueueData formData) {
		return getAfyaStatQueueDataDao().saveData(formData);
	}
	
	/**
	 * Delete form data from the database.
	 * 
	 * @param formData the form data
	 * @should remove form data from the database
	 */
	@Override
	public void purgeQueueData(final AfyaStatQueueData formData) {
		getAfyaStatQueueDataDao().purgeData(formData);
	}
	
	/**
	 * Get the total number of the queue data in the database with partial matching search term on
	 * the payload.
	 * 
	 * @param search the search term.
	 * @return the total number of the queue data in the database.
	 */
	@Override
	public Number countQueueData(final String search) {
		return afyaStatQueueDataDao.countData(search);
	}
	
	/**
	 * Get queue data with matching search term for a particular page.
	 * 
	 * @param search the search term.
	 * @param pageNumber the page number.
	 * @param pageSize the size of the page.
	 * @return list of all queue data with matching search term for a particular page.
	 */
	@Override
	public List<AfyaStatQueueData> getPagedQueueData(final String search, final Integer pageNumber, final Integer pageSize) {
		return afyaStatQueueDataDao.getPagedData(search, pageNumber, pageSize);
	}
	
	/**
	 * Return the error data with the given id.
	 * 
	 * @param id the error data id.
	 * @return the error data with the matching id.
	 * @should return error data with matching id.
	 * @should return null when no error data with matching id.
	 */
	@Override
	public ErrorInfo getErrorData(final Integer id) {
		return getErrorInfoDao().getData(id);
	}
	
	/**
	 * Return the error data with the given uuid.
	 * 
	 * @param uuid the error data uuid.
	 * @return the error data with the matching uuid.
	 * @should return error data with matching uuid.
	 * @should return null when no error data with matching uuid.
	 */
	@Override
	public ErrorInfo getErrorDataByUuid(final String uuid) {
		return getErrorInfoDao().getDataByUuid(uuid);
	}
	
	/**
	 * Return the registration error data with the given patientUuid.
	 * 
	 * @param patientUuid the error data uuid.
	 * @return the registration error data with the matching patientUuid.
	 * @should return registration error data with matching patientUuid.
	 * @should return null when no registration error data with matching patientUuid.
	 */
	@Override
	public ErrorInfo getRegistrationErrorDataByPatientUuid(String patientUuid) {
		
		List<ErrorInfo> errors = getErrorInfoDao().getPagedData(patientUuid, null, null);
		for (ErrorInfo errorData : errors) {
			if (StringUtils.equals("json-registration", errorData.getDiscriminator())) {
				return errorData;
			}
		}
		return null;
	}
	
	/**
	 * Return all saved error data.
	 * 
	 * @return all saved error data.
	 * @should return empty list when no error data are saved in the database.
	 * @should return all saved error data.
	 */
	@Override
	public List<ErrorInfo> getAllErrorData() {
		return getErrorInfoDao().getAllData();
	}
	
	/**
	 * Save error data into the database.
	 * 
	 * @param errorInfo the error data.
	 * @return saved error data.
	 * @should save error data into the database.
	 */
	@Override
	public ErrorInfo saveErrorData(final ErrorInfo errorInfo) {
		return getErrorInfoDao().saveData(errorInfo);
	}
	
	/**
	 * Delete error data from the database.
	 * 
	 * @param errorInfo the error data
	 * @should remove error data from the database
	 */
	@Override
	public void purgeErrorData(final ErrorInfo errorInfo) {
		getErrorInfoDao().purgeData(errorInfo);
	}
	
	/**
	 * Get the total number of the error data in the database with partial matching search term on
	 * the payload.
	 * 
	 * @param search the search term.
	 * @return the total number of the error data in the database.
	 */
	@Override
	public Number countErrorData(final String search) {
		return errorInfoDao.countData(search);
	}
	
	/**
	 * Get error data with matching search term for a particular page.
	 * 
	 * @param search the search term.
	 * @param pageNumber the page number.
	 * @param pageSize the size of the page.
	 * @return list of all error data with matching search term for a particular page.
	 */
	@Override
	public List<ErrorInfo> getPagedErrorData(final String search, final Integer pageNumber, final Integer pageSize) {
		return errorInfoDao.getPagedData(search, pageNumber, pageSize);
	}
	
	/**
	 * Return the archive data with the given id.
	 * 
	 * @param id the archive data id.
	 * @return the archive data with the matching id.
	 * @should return archive data with matching id.
	 * @should return null when no archive data with matching id.
	 */
	@Override
	public ArchiveInfo getArchiveData(final Integer id) {
		return getArchiveInfoDao().getData(id);
	}
	
	/**
	 * Return the archive data with the given uuid.
	 * 
	 * @param uuid the archive data uuid.
	 * @return the archive data with the matching uuid.
	 * @should return archive data with matching uuid.
	 * @should return null when no archive data with matching uuid.
	 */
	@Override
	public ArchiveInfo getArchiveDataByUuid(final String uuid) {
		return getArchiveInfoDao().getDataByUuid(uuid);
	}
	
	@Override
	public List<ArchiveInfo> getArchiveDataByFormDataUuid(final String formDataUuid) {
		return getArchiveInfoDao().getAllDataByFormDataUuid(formDataUuid);
	}
	
	@Override
	public List<ErrorInfo> getErrorDataByFormDataUuid(final String formDataUuid) {
		return getErrorInfoDao().getAllDataByFormDataUuid(formDataUuid);
	}
	
	@Override
	public List<AfyaStatQueueData> getQueueDataByFormDataUuid(final String formDataUuid) {
		return getAfyaStatQueueDataDao().getAllDataByFormDataUuid(formDataUuid);
	}
	
	/**
	 * Return all saved archive data.
	 * 
	 * @return all saved archive data.
	 * @should return empty list when no archive data are saved in the database.
	 * @should return all saved archive data.
	 */
	@Override
	public List<ArchiveInfo> getAllArchiveData() {
		return getArchiveInfoDao().getAllData();
	}
	
	/**
	 * Save archive data into the database.
	 * 
	 * @param archiveInfo the archive data.
	 * @return saved archive data.
	 * @should save archive data into the database.
	 */
	@Override
	public ArchiveInfo saveArchiveData(final ArchiveInfo archiveInfo) {
		return getArchiveInfoDao().saveData(archiveInfo);
	}
	
	/**
	 * Delete archive data from the database.
	 * 
	 * @param archiveInfo the archive data
	 * @should remove archive data from the database
	 */
	@Override
	public void purgeArchiveData(final ArchiveInfo archiveInfo) {
		getArchiveInfoDao().purgeData(archiveInfo);
	}
	
	/**
	 * Get the total number of the archive data in the database with partial matching search term on
	 * the payload.
	 * 
	 * @param search the search term.
	 * @return the total number of the archive data in the database.
	 */
	@Override
	public Number countArchiveData(final String search) {
		return archiveInfoDao.countData(search);
	}
	
	/**
	 * Get archive data with matching search term for a particular page.
	 * 
	 * @param search the search term.
	 * @param pageNumber the page number.
	 * @param pageSize the size of the page.
	 * @return list of all archive data with matching search term for a particular page.
	 */
	@Override
	public List<ArchiveInfo> getPagedArchiveData(final String search, final Integer pageNumber, final Integer pageSize) {
		return archiveInfoDao.getPagedData(search, pageNumber, pageSize);
	}
	
	/**
	 * Return the data source with the given id.
	 * 
	 * @param id the data source id.
	 * @return the data source with the matching id.
	 * @should return data source with matching id.
	 * @should return null when no data source with matching id.
	 */
	@Override
	public AfyaDataSource getDataSource(final Integer id) {
		return getAfyaDataSourceDao().getById(id);
	}
	
	/**
	 * Return the data source with the given uuid.
	 * 
	 * @param uuid the data source uuid.
	 * @return the data source with the matching uuid.
	 * @should return data source with matching uuid.
	 * @should return null when no data source with matching uuid.
	 */
	@Override
	public AfyaDataSource getDataSourceByUuid(final String uuid) {
		return getAfyaDataSourceDao().getDataSourceByUuid(uuid);
	}
	
	/**
	 * Return all saved data source.
	 * 
	 * @return all saved data source .
	 * @should return empty list when no data source are saved in the database.
	 * @should return all saved data source.
	 */
	@Override
	public List<AfyaDataSource> getAllDataSource() {
		return getAfyaDataSourceDao().getAll();
	}
	
	/**
	 * Save data source into the database.
	 * 
	 * @param afyaDataSource the data source.
	 * @return saved data source.
	 * @should save data source into the database.
	 */
	@Override
	public AfyaDataSource saveDataSource(final AfyaDataSource afyaDataSource) {
		return getAfyaDataSourceDao().saveOrUpdate(afyaDataSource);
	}
	
	/**
	 * Delete data source from the database.
	 * 
	 * @param dataSource the data source
	 * @should remove data source from the database
	 */
	@Override
	public void purgeDataSource(final AfyaDataSource dataSource) {
		getAfyaDataSourceDao().delete(dataSource);
	}
	
	/**
	 * Get the total number of the data source in the database with partial matching search term on
	 * the payload.
	 * 
	 * @param search the search term.
	 * @return the total number of the data source in the database.
	 */
	@Override
	public Number countDataSource(final String search) {
		return afyaDataSourceDao.countDataSource(search);
	}
	
	/**
	 * Get data source with matching search term for a particular page.
	 * 
	 * @param search the search term.
	 * @param pageNumber the page number.
	 * @param pageSize the size of the page.
	 * @return list of all data source with matching search term for a particular page.
	 */
	@Override
	public List<AfyaDataSource> getPagedDataSource(final String search, final Integer pageNumber, final Integer pageSize) {
		return afyaDataSourceDao.getPagedDataSources(search, pageNumber, pageSize);
	}
	
	/**
	 * Return the notification data with the given id.
	 * 
	 * @param id the notification data id.
	 * @return the notification data with the matching id.
	 * @should return notification data with matching id.
	 * @should return null when no notification data with matching id.
	 */
	@Override
	public NotificationInfo getNotificationData(final Integer id) {
		return getNotificationInfoDao().getData(id);
	}
	
	/**
	 * Return the notification data with the given uuid.
	 * 
	 * @param uuid the notification data uuid.
	 * @return the notification data with the matching uuid.
	 * @should return notification data with matching uuid.
	 * @should return null when no notification data with matching uuid.
	 */
	@Override
	public NotificationInfo getNotificationDataByUuid(final String uuid) {
		return getNotificationInfoDao().getDataByUuid(uuid);
	}
	
	/**
	 * Return all saved notification data.
	 * 
	 * @return all saved notification data.
	 * @should return empty list when no notification data are saved in the database.
	 * @should return all saved notification data.
	 */
	@Override
	public List<NotificationInfo> getAllNotificationData() {
		return getNotificationInfoDao().getAllData();
	}
	
	/**
	 * Return paged notification data for a particular person with matching search term for a
	 * particular page.
	 * 
	 * @param search the search term.
	 * @param pageNumber the page number.
	 * @param pageSize the size of the page.
	 * @return all saved notification data.
	 * @should return empty list when no notification data are saved in the database.
	 * @should return all saved notification data.
	 */
	@Override
	public List<NotificationInfo> getNotificationDataByReceiver(final Person person, final String search,
	        final Integer pageNumber, final Integer pageSize, final String status, final Date syncDate) {
		return getNotificationInfoDao().getNotificationsByReceiver(person, search, pageNumber, pageSize, status, syncDate);
	}
	
	/**
	 * Return paged notification data from a particular person with matching search term for a
	 * particular page.
	 * 
	 * @param search the search term.
	 * @param pageNumber the page number.
	 * @param pageSize the size of the page.
	 * @return all saved notification data.
	 * @should return empty list when no notification data are saved in the database.
	 * @should return all saved notification data.
	 */
	@Override
	public List<NotificationInfo> getNotificationDataBySender(final Person person, final String search,
	        final Integer pageNumber, final Integer pageSize, final String status, final Date syncDate) {
		return getNotificationInfoDao().getNotificationsBySender(person, search, pageNumber, pageSize, status, syncDate);
	}
	
	/**
	 * Return count for the paged notification data for a particular person with matching search
	 * term for a particular page.
	 * 
	 * @param person the person.
	 * @param search the search term.
	 * @return all saved notification data.
	 * @should return empty list when no notification data are saved in the database.
	 * @should return all saved notification data.
	 */
	@Override
	public Number countNotificationDataByReceiver(final Person person, final String search, final String status) {
		return getNotificationInfoDao().countNotificationsByReceiver(person, search, status);
	}
	
	/**
	 * Return count for the paged notification data from a particular person with matching search
	 * term for a particular page.
	 * 
	 * @param person the person.
	 * @param search the search term.
	 * @return all saved notification data.
	 * @should return empty list when no notification data are saved in the database.
	 * @should return all saved notification data.
	 */
	@Override
	public Number countNotificationDataBySender(final Person person, final String search, final String status) {
		return getNotificationInfoDao().countNotificationsBySender(person, search, status);
	}
	
	@Override
	public List<NotificationInfo> getNotificationDataByRole(final Role role, final String search, final Integer pageNumber,
	        final Integer pageSize, final String status) {
		return getNotificationInfoDao().getNotificationsByRole(role, search, pageNumber, pageSize, status);
	}
	
	@Override
	public Number countNotificationDataByRole(final Role role, final String search, final String status) {
		return getNotificationInfoDao().countNotificationsByRole(role, search, status);
	}
	
	/**
	 * Save notification data into the database.
	 * 
	 * @param notificationInfo the notification data.
	 * @return saved notification data.
	 * @should save notification data into the database.
	 */
	@Override
	public NotificationInfo saveNotificationData(final NotificationInfo notificationInfo) {
		return getNotificationInfoDao().saveOrUpdate(notificationInfo);
	}
	
	/**
	 * Delete notification data from the database.
	 * 
	 * @param notificationInfo the notification data
	 * @should remove notification data from the database
	 */
	@Override
	public void purgeNotificationData(final NotificationInfo notificationInfo) {
		getNotificationInfoDao().purgeData(notificationInfo);
	}
	
	/**
	 * Void a single notification data.
	 * 
	 * @param notificationInfo the notification data to be voided.
	 * @return the voided notification data.
	 */
	@Override
	public NotificationInfo voidNotificationData(final NotificationInfo notificationInfo, final String reason) {
		notificationInfo.setVoided(Boolean.TRUE);
		notificationInfo.setVoidedBy(Context.getAuthenticatedUser());
		notificationInfo.setDateVoided(new Date());
		notificationInfo.setVoidReason(reason);
		return saveNotificationData(notificationInfo);
	}
	
	@Override
	public ErrorMessagesInfo getErrorMessage(Integer id) {
		return getErrorMessagesInfoDao().getById(id);
	}
	
	@Override
	public ErrorMessagesInfo getErrorMessageByUuid(String uuid) {
		return getErrorMessagesInfoDao().getDataByUuid(uuid);
	}
	
	@Override
	public List<ErrorMessagesInfo> getAllErrorMessage() {
		return getErrorMessagesInfoDao().getAll();
	}
	
	@Override
	public ErrorMessagesInfo saveErrorMessage(ErrorMessagesInfo errorMessagesInfo) {
		return getErrorMessagesInfoDao().saveData(errorMessagesInfo);
	}
	
	@Override
	public void purgeErrorMessage(ErrorMessagesInfo errorMessagesInfo) {
		getErrorMessagesInfoDao().purgeData(errorMessagesInfo);
	}
	
	@Override
	public Number countErrorMessage(String search) {
		return getErrorMessagesInfoDao().countData(search);
	}
	
	@Override
	public List<ErrorMessagesInfo> getPagedErrorMessage(String search, Integer pageNumber, Integer pageSize) {
		return null;
	}
	
	@Override
	public List<ErrorMessagesInfo> validateData(String uuid, String formData) {
		List<ErrorMessagesInfo> errorMessages = new ArrayList<ErrorMessagesInfo>();
		ErrorInfo errorInfo = getErrorDataByUuid(uuid);
		errorInfoDao.detachDataFromHibernateSession(errorInfo);
		errorInfo.setPayload(formData);
		
		AfyaStatQueueData queueData = new AfyaStatQueueData(errorInfo);
		
		List<QueueInfoHandler> queueDataHandlers = HandlerUtil.getHandlersForType(QueueInfoHandler.class,
		    AfyaStatQueueData.class);
		for (QueueInfoHandler queueDataHandler : queueDataHandlers) {
			
			try {
				if (queueDataHandler.accept(queueData)) {
					queueDataHandler.validate(queueData);
				}
			}
			catch (Exception ex) {
				errorMessages = createErrorMessageList((StreamProcessorException) ex);
			}
		}
		
		return errorMessages;
	}
	
	@Override
	public List<String> getDiscriminatorTypes() {
		List<String> discriminatorTypes = new ArrayList<String>();
		List<QueueInfoHandler> queueDataHandlers = HandlerUtil.getHandlersForType(QueueInfoHandler.class,
		    AfyaStatQueueData.class);
		for (QueueInfoHandler queueDataHandler : queueDataHandlers) {
			String discriminator = queueDataHandler.getDiscriminator();
			// collect all discriminator value and return it to the web interface
			discriminatorTypes.add(discriminator);
		}
		return discriminatorTypes;
	}
	
	private List<ErrorMessagesInfo> createErrorMessageList(StreamProcessorException ex) {
		List<ErrorMessagesInfo> errorMessagesInfos = new ArrayList<ErrorMessagesInfo>();
		for (Exception exception : ex.getAllException()) {
			ErrorMessagesInfo error = new ErrorMessagesInfo();
			error.setMessage(exception.getMessage());
			errorMessagesInfos.add(error);
		}
		return errorMessagesInfos;
	}
	
	@Override
	public List<AfyaStatQueueData> mergeDuplicatePatient(@NotNull final String errorDataUuid,
	        @NotNull final String existingPatientUuid, @NotNull final String payload) {
		List<AfyaStatQueueData> requeued = new ArrayList<AfyaStatQueueData>();
		ErrorInfo errorInfo = this.getErrorDataByUuid(errorDataUuid);
		errorInfo.setPayload(payload);
		String submittedPatientUuid = errorInfo.getPatientUuid();
		
		errorInfo.setDiscriminator("json-demographics-update");
		
		errorInfo = this.saveErrorData(errorInfo);
		
		registerTemporaryUuid(submittedPatientUuid, existingPatientUuid);
		AfyaStatQueueData afyaStatQueueData = new AfyaStatQueueData(errorInfo);
		afyaStatQueueData = this.saveQueueData(afyaStatQueueData);
		this.purgeErrorData(errorInfo);
		requeued.add(afyaStatQueueData);
		
		// Fetch all ErrorData associated with the patient UUID (the one determined to be of a duplicate patient).
		int countOfErrors = this.countErrorData(submittedPatientUuid).intValue();
		List<ErrorInfo> allToRequeue = this.getPagedErrorData(submittedPatientUuid, 1, countOfErrors);
		for (ErrorInfo errorData1 : allToRequeue) {
			afyaStatQueueData = new AfyaStatQueueData(errorData1);
			afyaStatQueueData = this.saveQueueData(afyaStatQueueData);
			this.purgeErrorData(errorData1);
			requeued.add(afyaStatQueueData);
		}
		return requeued;
	}
	
	private void registerTemporaryUuid(final String temporaryUuid, final String permanentUuid) {
		RegistrationInfoService registrationDataService = Context.getService(RegistrationInfoService.class);
		RegistrationInfo registrationData = registrationDataService.getRegistrationDataByTemporaryUuid(temporaryUuid);
		if (registrationData == null) {
			registrationData = new RegistrationInfo();
			registrationData.setTemporaryUuid(temporaryUuid);
			registrationData.setAssignedUuid(permanentUuid);
			registrationDataService.saveRegistrationData(registrationData);
		}
	}
	
	public FormInfoStatus getFormDataStatusByFormDataUuid(String formDataUuid) {
		FormInfoStatus formDataStatus = new FormInfoStatus(formDataUuid);
		if (getArchiveDataByFormDataUuid(formDataUuid).size() > 0) {
			formDataStatus.setStatus("archived");
		} else if (getErrorDataByFormDataUuid(formDataUuid).size() > 0) {
			formDataStatus.setStatus("errored");
		} else if (getQueueDataByFormDataUuid(formDataUuid).size() > 0) {
			formDataStatus.setStatus("queued");
		} else {
			formDataStatus.setStatus("unknown");
		}
		return formDataStatus;
	}
}
