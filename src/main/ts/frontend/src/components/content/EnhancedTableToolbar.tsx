import React from 'react';
import clsx from 'clsx';
import { createStyles, lighten, makeStyles, Theme } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import DeleteIcon from '@material-ui/icons/Delete';
import PostAddIcon from '@material-ui/icons/PostAdd';
import SyncIcon from '@material-ui/icons/Sync';
import CircularProgress from '@material-ui/core/CircularProgress';

const useToolbarStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      paddingLeft: theme.spacing(2),
      paddingRight: theme.spacing(1),
    },
    highlight:
      theme.palette.type === 'light'
        ? {
          color: theme.palette.primary.main,
          backgroundColor: lighten(theme.palette.primary.light, 0.85),
        }
        : {
          color: theme.palette.text.primary,
          backgroundColor: theme.palette.secondary.dark,
        },
    title: {
      flex: '1 1 100%',
    },
  }),
);

interface EnhancedTableToolbarProps {
  numSelected: number;
  busy: boolean;
  queueSelected: boolean;
  onRefresh: () => void;
  onAddMessage: () => void;
  onDelete: () => void;
}

const EnhancedTableToolbar = (props: EnhancedTableToolbarProps) => {
  const classes = useToolbarStyles();
  const { numSelected, busy, queueSelected, onRefresh, onAddMessage, onDelete } = props;

  return (
    <Toolbar variant="dense"
      className={clsx(classes.root, {
        [classes.highlight]: numSelected > 0,
      })}
    >
      {numSelected > 0 ? (
        <Typography className={classes.title} color="inherit" variant="subtitle1" component="div">
          {numSelected} selected
        </Typography>
      ) : (
        <Typography className={classes.title} variant="h6" id="tableTitle" component="div">
          {busy ? (
            <CircularProgress size={20} disableShrink />
          ) : (
            <div>Content</div>
          )}
        </Typography>
      )}
      <IconButton aria-label="refresh" disabled={!queueSelected} onClick={onRefresh}>
        <Tooltip title="Refresh">
          <SyncIcon />
        </Tooltip>
      </IconButton>
      <IconButton aria-label="add" disabled={!queueSelected} onClick={onAddMessage}>
        <Tooltip title="Add">
          <PostAddIcon />
        </Tooltip>
      </IconButton>
      <IconButton aria-label="delete" disabled={numSelected <= 0} onClick={onDelete}>
        <Tooltip title="Delete">
          <DeleteIcon />
        </Tooltip>
      </IconButton>
    </Toolbar>
  );
};

export default EnhancedTableToolbar;