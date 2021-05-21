import React from 'react';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import Checkbox from '@material-ui/core/Checkbox';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import DescriptionOutlinedIcon from '@material-ui/icons/DescriptionOutlined';
import AlertDialog from '../../components/AlertDialog';
import PayloadDialog from '../../components/PayloadDialog';

import useStyles from '../../Styles';
import { Queue, Message, Payload } from '../../common/types';
import { Order, getComparator, stableSort } from './tableUtils';
import EnhancedTableToolbar from './EnhancedTableToolbar';
import EnhancedTableHead from './EnhancedTableHead';
import { fetchPayload} from '../../services/dbServices';


interface QueueContentProps {
    busy: boolean;
    queue: Queue | undefined;
    messages: Message[];
    onRefresh: () => void;
    onAddMessage: (payload: string) => void;
    onDeleteMessages: (ids: string[]) => void;
} 

export default function QueueContent({ busy, queue, messages, onRefresh, onAddMessage, onDeleteMessages }: QueueContentProps) {
  const classes = useStyles();

  // Store the parameters from the previous call
  const [prevQueue, setPrevQueue] = React.useState<Queue>();
  const [prevMessages, setPrevMessages] = React.useState<Message[]>();

  const [order, setOrder] = React.useState<Order>('desc');
  const [orderBy, setOrderBy] = React.useState<keyof Message>('timestamp');
  const [selected, setSelected] = React.useState<string[]>([]);
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);
  const [payload, setPayload] = React.useState<Payload>(); // for showing the payload in dialog
  const [alertDialogOpen, setAlertDialogOpen] = React.useState(false);
  const [dialogEditMode, setDialogEditMode] = React.useState(false);
  const [dialogOpen, setDialogOpen] = React.useState(false); // payload dialog

  const handleRequestSort = (event: React.MouseEvent<unknown>, property: keyof Message) => {
    const isAsc = orderBy === property && order === 'asc';
    setOrder(isAsc ? 'desc' : 'asc');
    setOrderBy(property);
  };

  const handleSelectAllClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.checked) {
      const newSelecteds = messages.map((n) => n.messageID);
      setSelected(newSelecteds);
      return;
    }
    setSelected([]);
  };

  const handleClick = (event: React.MouseEvent<unknown>, id: string) => {
    const selectedIndex = selected.indexOf(id);
    let newSelected: string[] = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, id);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selected.slice(0, selectedIndex),
        selected.slice(selectedIndex + 1),
      );
    }

    setSelected(newSelected);
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const isSelected = (id: string) => selected.indexOf(id) !== -1;

  const emptyRows = rowsPerPage - Math.min(rowsPerPage, messages.length - page * rowsPerPage);

  const deleteRecords = () => onDeleteMessages(selected); // Action when pressed OK
  const handleDelete = () => setAlertDialogOpen(true); // show dialogbox
  
  const handleAddMessage = () => {
      setPayload({ payload: ""});
      setDialogEditMode(true);
      setDialogOpen(true);
  };

  const handleRefresh = () => {
    onRefresh();
  }

  const showPayload = (message : Message) => {
    if (queue) {
      fetchPayload(queue!, message).then(pl => {
        setPayload(pl);
        setDialogEditMode(false);
        setDialogOpen(true);
      });
    }
  };

  const initState = () => {
    setPrevQueue(queue);
    setPrevMessages(messages);
    setSelected([]);
    setPage(0);
  };

  // The main code is executed each time the user clicks on a button, select a record, etc.
  // We want to init the state when a different queue is selected or the content (records) is changed
  if (prevQueue !== queue || prevMessages !== messages) {
    initState();
  }

  return (
    <div className={classes.root}>
      <Paper className={classes.paper}>
        <EnhancedTableToolbar
          numSelected={selected.length}
          busy={busy}
          queueSelected={queue ? true : false}
          onRefresh={handleRefresh}
          onAddMessage={handleAddMessage}
          onDelete={handleDelete}
        />
        <TableContainer>
          <Table
            className={classes.table}
            aria-labelledby="tableTitle"
            size="small"
            aria-label="enhanced table"
          >
            <EnhancedTableHead
              classes={classes}
              numSelected={selected.length}
              order={order}
              orderBy={orderBy}
              onSelectAllClick={handleSelectAllClick}
              onRequestSort={handleRequestSort}
              rowCount={messages.length}
            />
            <TableBody>
              {stableSort(messages, getComparator(order, orderBy))
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((message, index) => {
                  const isItemSelected = isSelected(message.messageID);
                  const labelId = `enhanced-table-checkbox-${index}`;

                  return (
                    <TableRow
                      key={message.messageID}
                      hover
                      role="checkbox"
                      aria-checked={isItemSelected}
                      tabIndex={-1}
                      selected={isItemSelected}
                      className={classes.tableRow} // change background when selected
                    >
                      <TableCell padding="checkbox" onClick={(event) => handleClick(event, message.messageID)}>
                        <Checkbox
                          checked={isItemSelected}
                          inputProps={{ 'aria-labelledby': labelId }}
                        />
                      </TableCell>
                      <TableCell component="th" id={labelId} scope="row" padding="none" onClick={(event) => handleClick(event, message.messageID)}>
                        {message.messageID}
                      </TableCell>
                      <TableCell padding="none" onClick={(event) => handleClick(event, message.messageID)}>
                        {message.timestamp}
                        </TableCell>
                      <TableCell>
                        <Tooltip title="Payload">
                          <IconButton size="small" aria-label="payload" onClick={() => { showPayload(message)}}>
                            <DescriptionOutlinedIcon />
                          </IconButton>
                        </Tooltip>
                      </TableCell>
                    </TableRow>
                  );
                })}
              {emptyRows > 0 && (
                <TableRow style={{ height: (41 * emptyRows) }}> {/* small: 33 medium: 53 */}
                  <TableCell colSpan={4} />
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[5, 10, 20]}
          component="div"
          count={messages.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onChangePage={handleChangePage}
          onChangeRowsPerPage={handleChangeRowsPerPage}
        />
      </Paper>
      <AlertDialog 
        title="Delete selected record(s)?" 
        message="You cannot undo this operation." 
        open={alertDialogOpen}
        setOpen={setAlertDialogOpen}
        action={deleteRecords}
      />
       <PayloadDialog 
        title={dialogEditMode ? "Submit New Payload" : "Show Payload"} 
        payload={payload?.payload}
        open={dialogOpen}
        editMode={dialogEditMode}
        setOpen={setDialogOpen}
        action={onAddMessage}
      />     
    </div>
  );
}